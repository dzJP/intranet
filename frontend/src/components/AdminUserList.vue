<template>
    <table class="table w-75 mx-auto">
        <thead>
            <tr>
                <th scope="col">Email</th>
                <th scope="col">First name</th>
                <th scope="col">Last name</th>
                <th scope="col">Phone number</th>
                <th scope="col">Actions</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="user in user.users" :key="user.email">
                <td><input v-model="user.email" :disabled="!user.editMode"></td>
                <td><input v-model="user.firstName" :disabled="!user.editMode"></td>
                <td><input v-model="user.lastName" :disabled="!user.editMode"></td>
                <td><input v-model="user.phoneNumber" :disabled="!user.editMode"></td>
                <td>
                    <button class="btn btn-primary" @click="saveChanges(user)">Spara</button>
                    <button class="btn btn-primary" @click="toggleEditMode(user)">Redigera</button>
                    <button class="btn btn-primary" @click="deactivateUser(user)">Inaktivera</button>
                    <button class="btn btn-primary" @click="activateUser(user)">Aktivera</button>
                    <button class="btn btn-danger" @click="deleteUser(user)">Ta bort</button>
                </td>
            </tr>
        </tbody>
    </table>
</template>

<script>
import { onMounted } from 'vue';
import { useUserStore } from '@/stores/user';

export default {
    setup() {
        const userStore = useUserStore();

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
        };
    },
};
</script>