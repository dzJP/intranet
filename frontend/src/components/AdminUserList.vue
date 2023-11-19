<template>
  <div class="mx-auto justify-content-center">
    <input v-model="searchQuery" placeholder="Search by name" class="search-input" />
  </div>
  <table class="mx-auto">
    <!-- <thead>
        <tr>
          <th scope="col">Email</th>
          <th scope="col">First name</th>
          <th scope="col">Last name</th>
          <th scope="col">Phone number</th>
        </tr>
      </thead> -->
    <tbody>
      <tr v-for="user in filteredUsers" :key="user.email">
        <td><input v-model="user.email" disabled></td>
        <td><input v-model="user.firstName" :disabled="!user.editMode"></td>
        <td><input v-model="user.lastName" :disabled="!user.editMode"></td>
        <td><input v-model="user.phoneNumber" :disabled="!user.editMode"></td>
        <td>
          <router-link :to="`/user/${user.email}`">
            <button class="btn btn-primary">
              <i class="bi bi-person"></i>
            </button>
          </router-link>
          <button class="btn btn-secondary" @click="toggleEditMode(user)">
            <i class="bi bi-pencil"></i>
          </button>
          <button class="btn btn-primary" @click="saveChanges(user)">
            <i class="bi bi-floppy"></i>
          </button>
          <button class="btn btn-secondary" @click="deactivateUser(user)">
            Avaktivera
          </button>
          <button class="btn btn-primary" @click="activateUser(user)">
            Aktivera
          </button>
          <input v-model="user.currentPassword" type="password" placeholder="Current Password" :disabled="!user.editMode">
          <input v-model="user.newPassword" type="password" placeholder="New Password" :disabled="!user.editMode">
          <button @click="changePassword(user)">Change Password</button>
          <button class="btn btn-danger" @click="deleteUser(user)">
            <i class="bi bi-trash"></i>
          </button>
        </td>
      </tr>
    </tbody>
  </table>

  <div class="text-center">
    <button @click="loadMoreUsers" v-if="filteredUsers.length >= 10">
      Load More
    </button>
  </div>
</template>
  
<script>
import { onMounted, ref, computed } from 'vue';
import { useUserStore } from '@/stores/user';

export default {
  setup() {
    const userStore = useUserStore();
    const searchQuery = ref('');
    const newPassword = ref('');
    const currentPassword = ref('');
    const displayedUsersCount = ref(10);

    const getUsers = async () => {
      try {
        await userStore.getAllUsers();
      } catch (error) {
        console.error('Error fetching user profiles:', error);
      }
    };

    const saveChanges = async (user) => {
      try {
        await userStore.editUser(user);
        toggleEditMode(user);
        getUsers();
      } catch (error) {
        console.error('Error updating user:', error);
      }
    };

    const toggleEditMode = (user) => {
      user.editMode = !user.editMode;
    };

    const editUser = (user) => {
      userStore.setEmail(user.email);
      userStore.setFirstName(user.firstName);
      userStore.setLastName(user.lastName);
      userStore.setPhoneNumber(user.phoneNumber);
    };

    const deleteUser = async (user) => {
      try {
        await userStore.deleteUser(user);
        getUsers();
      } catch (error) {
        console.error('Error deleting user:', error);
      }
    };

    const deactivateUser = async (user) => {
      try {
        await userStore.deactivateUser(user);
        getUsers();
      } catch (error) {
        console.error('Error deactivating user:', error);
      }
    };

    const activateUser = async (user) => {
      try {
        await userStore.activateUser(user);
        getUsers();
      } catch (error) {
        console.error('Error activating user:', error);
      }
    };

    const filteredUsers = computed(() => {
      const allUsers = userStore.searchUsers(searchQuery.value);
      return allUsers.slice(0, displayedUsersCount.value); // Display only the specified number of users
    });

    const setUserPassword = (user, password) => {
  user.currentPassword = password.current;
  user.newPassword = password.new;
};

const changePassword = async (user) => {
  try {
    await userStore.changePassword(user, user.currentPassword, user.newPassword);

    user.newPassword = '';
    user.currentPassword = '';
  } catch (error) {
    console.error('Error changing password:', error);
  }
};

const loadMoreUsers = () => {
      displayedUsersCount.value += 10; // Load 10 more users
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
      toggleEditMode,
      searchQuery,
      filteredUsers,
      loadMoreUsers,
      newPassword,
      currentPassword,
      changePassword,
      setUserPassword,
    };
  },
};
</script>

<style scoped></style>