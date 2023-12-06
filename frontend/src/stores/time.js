import axios from "axios";
import { defineStore } from "pinia";
import { useAuthStore } from "./auth";

export const useTimeStore = defineStore({
    id: "time",
  state: () => ({
    email: '',
    workHours: '',
    date: '',
    timeRegistrations: [],
    totalTime: null,
  }),
  actions: {
    async registerTime() {
      try {
        const auth = useAuthStore();
        const token = auth.token;

        console.log("Email:", auth.user);
        console.log("Work Hours:", this.workHours);
        console.log("Date:", this.date);

        const response = await axios.post(
          "http://localhost:8080/api/v1/register-time",
          {
            email: auth.user,
            workHours: this.workHours,
            date: this.date,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        console.log("Registration successful:", response.data);

        await this.getTimeRegistrations();
        await this.getTotalTimeForCurrentMonth(auth.user);

        this.email = '';
        this.workHours = '';
        this.date = '';
        
      } catch (error) {
        console.error(
          "Error registering time:",
          error.response || error.message
        );
      }
    },
    
    async getTimeRegistrations() {
      try {
        const auth = useAuthStore();
        const token = auth.token;

        const response = await axios.get(
          "http://localhost:8080/api/v1/time-registrations",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
            params: {
              email: auth.user,
            },
          }
        );

        console.log("Time registrations fetched successfully:", response.data);

        this.timeRegistrations = response.data;
      } catch (error) {
        console.error(
          "Error fetching time registrations:",
          error.response || error.message
        );
      }
    },
    async getTotalTimeForCurrentMonth(userEmail) {
      try {
        const response = await axios.get(
          "http://localhost:8080/api/v1/total-time-this-month",
          {
            params: {
              userEmail,
            },
          }
        );

        console.log("Total time for this month:", response.data);
        this.totalTime = response.data;
      } catch (error) {
        console.error(
          "Error fetching total time for this month:",
          error.response || error.message
        );
      }
    },
    async clearTimeRegistrations() {
      this.timeRegistrations = [];
      this.totalTime = null;
    },
  },
});