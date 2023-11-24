<template>
    <div>
        <label for="totalWorkHours">Total Work Hours:</label>
        <input type="text" id="totalWorkHours" v-model="totalWorkHoursInput" />
        <button @click="registerTime">Register Time</button>
    </div>
</template>

<script>
import axios from "axios";
import { useUserStore } from "@/stores/user";

export default {
    data() {
        return {
            totalWorkHoursInput: "",
        };
    },
    async created() {
        try {
            const userStore = useUserStore();
            await userStore.getUserDetails();
            console.log(userStore.email);
        } catch (error) {
            console.error("Error fetching user details:", error);
        }
    },
    methods: {
        async registerTime() {
            try {
                
                const response = await axios.post("http://localhost:8081/api/v1/register-time", {
                    totalWorkHours: this.totalWorkHoursInput,
                });

                console.log(response.data);
            } catch (error) {
                console.error("Error registering time:", error);
            }
        },
    },
};
</script>
