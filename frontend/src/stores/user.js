import { defineStore } from "pinia";
import axios from "axios";
import { useAuthStore } from "./auth";

export const useUserStore = defineStore({
  id: "user",
  state: () => ({
    email: "",
    firstName: "",
    lastName: "",
    phoneNumber: "",
    birthDate: "",
    users: [],
  }),
  actions: {
    async getUserDetails() {
      try {
        const auth = useAuthStore();
				const token = auth.token;

          const response = await axios.get('http://localhost:8080/api/v1/profile', {
              headers: {
                  Authorization: `Bearer ${token}`,
              },
              params: {
                  email: auth.user,
              },
          });

          const userDetails = response.data;

          this.email = userDetails.email;
          this.firstName = userDetails.firstName;
          this.lastName = userDetails.lastName;
          this.phoneNumber = userDetails.phoneNumber;
          this.birthDate = userDetails.birthDate;

          console.log('User details:', userDetails);
      } catch (error) {
          console.error('Error fetching user details:', error);
      }
  },
    async getAllUsers() {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/admin", {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });

        this.users = response.data;

        console.log("User details:", this.users);
      } catch (error) {
        console.error("Error fetching user details:", error);
      }
    },
    async editUser(user) {
      try {
        const response = await axios.put(
          `http://localhost:8080/api/v1/admin/edit-user/${user.email}`,
          user,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );

        console.log("User edited successfully:", response.data);
      } catch (error) {
        console.error("Error editing user:", error);
        throw error;
      }
    },
    async deleteUser(user) {
      try {
        const response = await axios.delete(
          `http://localhost:8080/api/v1/admin/delete-user/${user.email}`,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );

        console.log("User deleted successfully:", response.data);

        this.users = this.users.filter((user) => user.email !== user.email);
      } catch (error) {
        console.error("Error deleting user:", error);
        throw error;
      }
    },
    async updatePhoneNumber(newPhoneNumber) {
      try {
        const response = await axios.put(
          "http://localhost:8080/api/v1/profile/update-phone-number",
          {
            phoneNumber: newPhoneNumber,
          },
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );

        this.phoneNumber = newPhoneNumber;

        console.log("Phone number updated successfully:", response.data);
      } catch (error) {
        console.error("Error updating phone number:", error);
        throw error;
      }
    },

    async deactivateUser(user) {
      try {
        const response = await axios.patch(
          `http://localhost:8080/api/v1/admin/deactivate-user/${user.email}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );

        console.log("User deactivated successfully:", response.data);

        await this.getAllUsers();
      } catch (error) {
        console.error("Error deactivating user:", error);
        throw error;
      }
    },

    async activateUser(user) {
      try {
        const response = await axios.patch(
          `http://localhost:8080/api/v1/admin/activate-user/${user.email}`,
          null,
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );

        console.log("User activated successfully:", response.data);

        await this.getAllUsers();
      } catch (error) {
        console.error("Error activating user:", error);
        throw error;
      }
    },
    searchUsers(query) {
      if (!query) {
        return this.users;
      } else {
        return this.users.filter(
          (user) =>
            user.firstName.toLowerCase().includes(query.toLowerCase()) ||
            user.lastName.toLowerCase().includes(query.toLowerCase())
        );
      }
    },
    async changePassword(user, currentPassword, newPassword) {
      try {
        const response = await axios.patch(
          `http://localhost:8080/api/v1/admin/change-password/${user.email}`,
          {
            currentPassword,
            newPassword,
          },
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );

        console.log("Password changed successfully:", response.data);
        return response.data;
      } catch (error) {
        console.error("Error changing password:", error);
        throw error;
      }
    },
    logout() {
      this.email = "";
      this.firstName = "";
      this.lastName = "";
      this.phoneNumber = "";
    },
  },
});

export const fetchUserProfile = async (email) => {
    try {
        const response = await axios.get(`http://localhost:8080/api/v1/user/${email}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
            },
        });

        console.log('User details response:', response.data);

        return response.data;
    } catch (error) {
        console.error('Error fetching user details:', error);
        throw error;
    }
};