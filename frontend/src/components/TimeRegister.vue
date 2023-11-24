<template>
    <div>
        <label for="email">Email:</label>
        <input type="text" id="email" v-model="email" />

        <label for="workHours">Work Hours:</label>
        <input type="text" id="workHours" v-model="workHours" />

        <label for="date">Date:</label>
        <input type="date" id="date" v-model="date" />

        <button @click="registerTime">Register Time</button>
    </div>
</template>

<script>
import axios from "axios";
import { useAuthStore } from "@/stores/auth";

export default {
    data() {
        return {
            email: "",
            workHours: "",
            date: "",
        };
    },
    methods: {
        async registerTime() {
            try {
                const auth = useAuthStore();
                const token = auth.token;

                // Log the values to ensure they are correct
                console.log("Email:", this.email);
                console.log("Work Hours:", this.workHours);
                console.log("Date:", this.date);

                const response = await axios.post(
                    "http://localhost:8080/api/v1/register-time",
                    {
                        email: this.email,
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
            } catch (error) {
                console.error("Error registering time:", error.response || error.message);
            }
        },
    },
};
</script>
