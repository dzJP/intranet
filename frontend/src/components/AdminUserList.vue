<template>
  <div class="card userlist">
    <div>
    <SearchBarUsers @search="updateSearchQuery" />
    </div>
    <div class="card-body px-0 pt-0 pb-2">
      <div class="table-responsive p-0">
        <table class="table mb-0">
          <thead>
            <tr>
              <th
                class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
              >
                Employee
              </th>
              <th
                class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
              >
                Phone number
              </th>
              <th
                class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7"
              >
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
                <button class="btn btn-edit" @click="togglePopup(user)">
                  Change
                </button>
                <button class="btn btn-delete" @click="deleteUser(user)">
                  Remove
                </button>
              </td>

              <div v-if="isPopupVisible && selectedUser" class="admin-popup">
                <div class="popup-content">
                  <i class="admin-toggle bi bi-x" @click="togglePopup"></i>
                  <table class="table">
                    <tr>
                      <td><strong>Email:</strong> {{ selectedUser.email }}</td>
                    </tr>
                    <tr>
                      <td>
                        <strong>First name:</strong>
                        <input v-model="selectedUser.firstName" />
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <strong>Last name:</strong>
                        <input v-model="selectedUser.lastName" />
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <strong>Birthdate:</strong>
                        <input type="date" v-model="selectedUser.birthDate" />
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <strong>Phone number:</strong>
                        <input v-model="selectedUser.phoneNumber" />
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <strong>Role:</strong>
                        <input v-model="selectedUser.role" />
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <strong>Change password:</strong>
                        <br />
                        <input
                          v-model="selectedUser.currentPassword"
                          type="password"
                          placeholder="Current password"
                        />
                        <input
                          v-model="selectedUser.newPassword"
                          type="password"
                          placeholder="New password"
                        />
                        <button
                          class="btn btn-primary mt-2"
                          @click="changePassword(selectedUser)"
                        >
                          Change password
                        </button>
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <button
                          class="btn btn-deactivate"
                          @click="deactivateUser(selectedUser)"
                        >
                          Inactivate
                        </button>
                        <button
                          class="btn btn-primary"
                          @click="activateUser(selectedUser)"
                        >
                          Activate
                        </button>
                      </td>
                    </tr>
                    <tr>
                      <td class="bottom-right">
                        <button
                          class="btn btn-primary"
                          @click="togglePopup(user)"
                        >
                          Cancel
                        </button>
                        <button
                          class="btn btn-primary"
                          @click="saveChanges(selectedUser)"
                        >
                          Save
                        </button>
                      </td>
                    </tr>
                  </table>
                </div>
              </div>
            </tr>

            <div class="mx-auto">
              <button
                class="btn btn-primary"
                @click="loadMoreUsers"
                v-if="filteredUsers.length >= 10"
              >
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
.card {
  background-color: #fff;
  border: none;
}

.userlist {
  align-items: center;
  margin: 10px auto;
  width: 800px;
}

.table td,
.table th {
  border: none;
  text-align: left;
}

.table thead th {
  font-size: 14px;
}

.table input {
  width: 80%;
  border: none;
  border-bottom: 1px solid #eee;
}

.btn-primary {
  background-color: #2f2ff6;
  border-color: #2f2ff6;
  color: #fff;
}

.btn-delete,
.btn-edit,
.btn-deactivate {
  color: #454444;
  border-color: #454444;
}

.btn-edit:hover {
  color: #fff;
  background-color: #454444;
  border-color: #454444;
}

.btn-delete:hover,
.btn-deactivate:hover {
  color: #fff;
  background-color: #df3232;
  border-color: #f13d3d;
}

.admin-toggle {
  position: absolute;
  top: 0;
  right: 0;
  height: 35px;
  width: 40px;
  font-size: 25px;
  cursor: pointer;
  transition: 0.3s;
  text-align: center;
}

.admin-toggle:hover {
  background-color: #df3232;
  color: #fff;
  cursor: pointer;
}

.admin-popup {
  position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
  background: #fff;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 1;
}

.admin-popup input:focus {
  outline: none;
}

.bottom-right {
  position: absolute;
  bottom: 0;
  right: 0;
  margin-right: 10px;
}
</style>
