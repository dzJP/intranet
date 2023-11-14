import { defineStore } from 'pinia';
import axios from 'axios';

export const useUserStore = defineStore({
    id: 'user',
    state: () => ({
        email: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        users: [],
    }),
    actions: {
        async getUserDetails() {
            try {
                const response = await axios.get('http://localhost:8080/api/v1/profile', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                const userDetails = response.data;

                this.email = userDetails.email;
                this.firstName = userDetails.firstName;
                this.lastName = userDetails.lastName;
                this.phoneNumber = userDetails.phoneNumber;

                console.log('User details:', userDetails);
            } catch (error) {
                console.error('Error fetching user details:', error);
            }
        },
        async getAllUsers() {
            try {
                const response = await axios.get('http://localhost:8080/api/v1/admin', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                this.users = response.data;

                console.log('User details:', this.users);

            } catch (error) {
                console.error('Error fetching user details:', error);
            }
        },
        async editUser(user) {
            try {
                const response = await axios.put(`http://localhost:8080/api/v1/admin/edit-user/${user.email}`, user, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                console.log('User edited successfully:', response.data);
            } catch (error) {
                console.error('Error editing user:', error);
                throw error;
            }
        },
        async deleteUser(user) {
            try {
                const response = await axios.delete(`http://localhost:8080/api/v1/admin/delete-user/${user.email}`, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                console.log('User deleted successfully:', response.data);
                
                this.users = this.users.filter(user => user.email  !== user.email);
            } catch (error) {
                console.error('Error deleting user:', error);
                throw error;
            }
        },
        async updatePhoneNumber(newPhoneNumber) {
            try {
                const response = await axios.put('http://localhost:8080/api/v1/profile/update-phone-number', {
                    phoneNumber: newPhoneNumber,
                }, {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`,
                    },
                });

                // Update the local store with the new phone number
                this.phoneNumber = newPhoneNumber;

                console.log('Phone number updated successfully:', response.data);
            } catch (error) {
                console.error('Error updating phone number:', error);
                throw error;
            }
        },
        logout() {
            // Clear user data on logout
            this.email = '';
            this.firstName = '';
            this.lastName = '';
            this.phoneNumber = '';
        },
    },
});