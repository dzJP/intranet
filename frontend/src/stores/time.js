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
    async registerTime(projectId) {
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
            projectId: projectId,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        const newTimeRegistration = response.data;

        this.timeRegistrations.push(newTimeRegistration);

        // this.workHours = null;
        // this.selectedProject = null; 

        console.log("Registration successful:", response.data);

        const year = this.date.split("-")[0];
        const month = this.date.split("-")[1];

        await this.getTimeRegistrationsForSelectedMonth(year, month);
        await this.getTotalTimeForSelectedMonth(year, month);

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
    
    async getTimeRegistrationsForSelectedMonth(year, month) {
      try {
        const auth = useAuthStore();
        const token = auth.token;

        console.log("Email:", auth.user);
        console.log("Year: ", year);
        console.log("Month: ", month);

        const response = await axios.get(
          "http://localhost:8080/api/v1/time-registrations",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
            params: {
              email: auth.user,
              year,
              month,
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
    async getTotalTimeForSelectedMonth(year, month) {
      try {
        const auth = useAuthStore();
        const token = auth.token;


        console.log("Email:", auth.user);
        console.log("Year: ", year);
        console.log("Month: ", month);
      

        const response = await axios.get(
          "http://localhost:8080/api/v1/total-time-this-month",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
            params: {
              email: auth.user,
              year,
              month
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
    
    async deleteTimeRegistration(registrationId) {
      try {
        const auth = useAuthStore();
        const token = auth.token;

        console.log("Email:", auth.user);
        console.log("ID:", registrationId);

        const response = await axios.delete(
          `http://localhost:8080/api/v1/time-registrations/${registrationId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
            params: {
              email: auth.user,
            },
          }
        );

        console.log("Time registration deleted:", response.data);

        this.timeRegistrations = this.timeRegistrations.filter(
          (timeRegistration) => timeRegistration.id !== registrationId
        );

        const year = this.date.split("-")[0];
        const month = this.date.split("-")[1];

        await this.getTimeRegistrationsForSelectedMonth(year, month);
        await this.getTotalTimeForSelectedMonth(year, month);
      } catch (error) {
        console.error(
          "Error deleting time registration:",
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