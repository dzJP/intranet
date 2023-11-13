import { defineStore } from 'pinia';
import axios from 'axios';

export const useUserStore = defineStore({
  id: 'user',
  state: () => ({
    email: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',
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