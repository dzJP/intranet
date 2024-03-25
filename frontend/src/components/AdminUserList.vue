<template>
  <div>
    <SearchBarUsers @search="updateSearchQuery" class="search-bar" />
  </div>
  <div class="card userlist">
    <div class="card-body px-0 pt-0 pb-2">
      <div class="table-responsive p-0">
        <table class="table mb-0">
          <thead>
            <tr>
              <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                Employee
              </th>
              <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                Phone number
              </th>
              <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                Role
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in filteredUsers" :key="user.email">
              <td>
                <router-link :to="`/user/${user.email}`">
                  <h6 class="mb-0 text-sm">
                    {{ user.firstName + " " + user.lastName }}
                  </h6>
                </router-link>
                <p>{{ user.email }}</p>
              </td>
              <td>
                <p>{{ user.phoneNumber }}</p>
              </td>
              <td>
                <p>{{ user.role }}</p>
              </td>
              <td>
                <div class="button-container">
                  <button class="edit-button" @click="togglePopup(user)">
                    Change
                  </button>
                  <button class="delete-button" @click="deleteUser(user)">
                    Remove
                  </button>
                </div>
              </td>

              <div v-if="isPopupVisible && selectedUser" class="popup">
                <div class="popup-content">
                  <i class="toggle bi bi-x" @click="togglePopup"></i>
                  <table class="table">
                    <tr>
                      <td class="popup-label"><strong>Email:</strong></td>
                      <td><input class="popup-input" v-model="selectedUser.email" /></td>
                    </tr>
                    <tr>
                      <td class="popup-label"><strong>First name:</strong></td>
                      <td><input class="popup-input" v-model="selectedUser.firstName" /></td>
                    </tr>
                    <tr>
                      <td class="popup-label"><strong>Last name:</strong></td>
                      <td><input class="popup-input" v-model="selectedUser.lastName" /></td>
                    </tr>
                    <tr>
                      <td class="popup-label"><strong>Birthdate:</strong></td>
                      <td><input type="date" v-model="selectedUser.birthDate" /></td>
                    </tr>
                    <tr>
                      <td class="popup-label"><strong>Phone number:</strong></td>
                      <td><input class="popup-input" v-model="selectedUser.phoneNumber" /></td>
                    </tr>
                    <tr>
                      <td class="popup-label"><strong>Role:</strong></td>
                      <td><input class="popup-input" v-model="selectedUser.role" /></td>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <strong>Change password:</strong><br />
                        <input v-model="selectedUser.currentPassword" type="password" placeholder="Current password" />
                        <input v-model="selectedUser.newPassword" type="password" placeholder="New password" />
                        <button class="btn btn-change-password" @click="changePassword(selectedUser)">
                          Change password
                        </button>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="2">
                        <button class="btn btn-deactivate" @click="deactivateUser(selectedUser)">
                          Inactivate
                        </button>
                        <button class="btn btn-primary" @click="activateUser(selectedUser)">
                          Activate
                        </button>
                      </td>
                    </tr>
                    <tr>
                      <td colspan="2" class="bottom-right">
                        <button class="btn btn-primary" @click="togglePopup(user)">
                          Cancel
                        </button>
                        <button class="btn btn-primary" @click="saveChanges(selectedUser)">
                          Save
                        </button>
                      </td>
                    </tr>
                  </table>
                </div>
              </div>
            </tr>

            <div class="mx-auto">
              <button class="btn btn-primary" @click="loadMoreUsers" v-if="filteredUsers.length >= 10">
                Load More
              </button>
            </div>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { onMounted, ref, computed } from 'vue';
import { useUserStore } from '@/stores/user';
import SearchBarUsers from '@/components/SearchBarUsers.vue';

export default {
  components: {
    SearchBarUsers,
  },

  setup() {
    const userStore = useUserStore();
    const searchQuery = ref("");
    const newPassword = ref("");
    const currentPassword = ref("");
    const displayedUsersCount = ref(10);
    const isPopupVisible = ref(false);
    const selectedUser = ref(null);

    const getUsers = async () => {
      try {
        await userStore.getAllUsers();
      } catch (error) {
        console.error("Error fetching user profiles:", error);
      }
    };

    const togglePopup = (user) => {
      selectedUser.value = user;
      isPopupVisible.value = !isPopupVisible.value;
    };

    const saveChanges = async () => {
      if (selectedUser.value) {
        try {
          await userStore.editUser(selectedUser.value);
          getUsers();
          console.log("User updated:", selectedUser.value);
        } catch (error) {
          console.error("Error updating user:", error);
        }
      }
    };

    const editUser = (user) => {
      userStore.setEmail(user.email);
      userStore.setFirstName(user.firstName);
      userStore.setLastName(user.lastName);
      userStore.setPhoneNumber(user.phoneNumber);
      userStore.setRole(user.role);
    };

    const deleteUser = async (user) => {
      try {
        await userStore.deleteUser(user);
        getUsers();
      } catch (error) {
        console.error("Error deleting user:", error);
      }
    };

    const deactivateUser = async (user) => {
      try {
        await userStore.deactivateUser(user);
        getUsers();
      } catch (error) {
        console.error("Error deactivating user:", error);
      }
    };

    const activateUser = async (user) => {
      try {
        await userStore.activateUser(user);
        getUsers();
      } catch (error) {
        console.error("Error activating user:", error);
      }
    };

    const filteredUsers = computed(() => {
      const allUsers = userStore.searchUsers(searchQuery.value);
      return allUsers.slice(0, displayedUsersCount.value);
    });

    const updateSearchQuery = (value) => {
      searchQuery.value = value;
    };

    const setUserPassword = (user, password) => {
      user.currentPassword = password.current;
      user.newPassword = password.new;
    };

    const changePassword = async (user) => {
      try {
        await userStore.changePassword(
          user,
          user.currentPassword,
          user.newPassword
        );

        user.newPassword = "";
        user.currentPassword = "";
      } catch (error) {
        console.error("Error changing password:", error);
      }
    };

    const loadMoreUsers = () => {
      displayedUsersCount.value += 10;
    };

    onMounted(() => {
      getUsers();
    });

    return {
      user: userStore,
      saveChanges,
      editUser,
      deleteUser,
      deactivateUser,
      activateUser,
      searchQuery,
      filteredUsers,
      loadMoreUsers,
      newPassword,
      currentPassword,
      changePassword,
      setUserPassword,
      isPopupVisible,
      togglePopup,
      updateSearchQuery,
      selectedUser: selectedUser,
    };
  },
};
</script>

<style scoped>

.search-bar {
  background-color: #040B24;
  color: #fff;
  display: flex;
  width: 800px;
  margin: auto;
  margin-bottom: 20px;
  padding: 10px;
  border: 2px solid #111C44;
  border-radius: 1px;
  font-family: 'Oxanium', sans-serif;
}

.userlist {
  background-color: var(--dark-blue);
  border: 2px solid #111C44;
  border-radius: 1px;
  align-items: center;
  margin: 10px auto;
  width: 800px;
}

.userlist p {
  color: #ffffff;
  font-family: 'Oxanium', sans-serif;
}

.table strong {
  background-color: var(--dark-blue);
  font-family: 'Oxanium', sans-serif;
  color: #ffffff;
}

.table td,
.table th {
  background-color: var(--dark-blue);
  font-family: 'Oxanium', sans-serif;
  border: none;
  text-align: left;
}

.table thead th {
  font-size: 14px;
}

.table input {
  background-color: var(--dark-blue);
  box-sizing: border-box;
  color: #ffffff;
  width: 100%;
  border: none;
  border-bottom: 1px solid #eee;
}


.btn-primary,
.btn-change-password,
.btn-deactivate {
  border: none;
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  text-transform: uppercase;
  margin-top: 10px;
  cursor: pointer;
  font-family: 'Oxanium', sans-serif;
  transition: background-color 0.3s ease, color 0.3s ease;
  background-color: var(--light-blue);
}

.btn-primary:hover,
.btn:hover {
  background-color: #2d2dc7;

}

.btn-delete:hover,
.btn-deactivate:hover {
  color: #fff;
  background-color: #df3232;
}


.button-container {
  display: flex;
}

.edit-button,
.delete-button {
  display:flex;
  text-align: center;
  text-transform: uppercase;
  justify-content: center;
  font-family: 'Oxanium', sans-serif;
  font-size: 12px;
  letter-spacing: 2px;
  height: 35px;
  cursor: pointer;
  box-shadow: 1px 1px 8px 1px #2525A5;
  transition: background-color 0.3s ease, color 0.3s ease;
  background-color: var(--light-blue);
}

.edit-button:hover,
.delete-button:hover {
  color: #fff;
  background-color: var(--light-blue-hover);
}

.toggle {
  position: absolute;
  top: 0;
  right: 0;
  height: 35px;
  width: 40px;
}

.toggle:hover {
  background-color: #df3232;
  color: #fff;
  border-radius: 10px;
  cursor: pointer;
}

.toggle i {
  font-size: 25px;
  color: #454444;
  cursor: pointer;
  transition: 0.3s;
}

.popup {
  background-color: var(--dark-blue);
  position: absolute;
  margin: auto;
  width: 100%;
  height: auto;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

}

input:focus {
  outline: none;
}

.popup-label {
  color: #fff;
  width: 30%;
}

.popup-input {
  background-color: var(--dark-blue);
  border: none;
  border-bottom: 1px solid #eee;
  width: 70%;
}

.bottom-right {
  position: absolute;
  bottom: 0;
  right: 0;
  margin-right: 10px;
}
</style>