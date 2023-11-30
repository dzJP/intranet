<template>
	<div>
		<h2>Monthly Time Overview</h2>

		<label for="selectedMonth">Select Month:</label>
		<select id="selectedMonth" v-model="selectedMonth" @change="fetchMonthlyTotals">
			<option v-for="(month, index) in months" :key="index" :value="index + 1">
				{{ month }}
			</option>
		</select>

		<div v-if="timeRegistrations && timeRegistrations.length > 0">
			<h3>Time Registrations:</h3>
			<ul>
				<li v-for="registration in timeRegistrations" :key="registration.id">
					{{ registration.date }} - {{ registration.workHours }} hours
					({{ registration.email }})
				</li>
			</ul>
		</div>
		<div v-else>
			<p>No time registrations found for {{ monthName(selectedMonth) }}.</p>
		</div>
		<button @click="resetTotalTime">Reset Total Time</button>
	</div>
</template>

<script>
import axios from "axios";
import { useAuthStore } from "@/stores/auth";

export default {
	data() {
		return {
			selectedMonth: new Date().getMonth() + 1, // Default to the current month
			timeRegistrations: [],
			totalTime: null, // Initialize totalTime
			months: [
				"January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December"
			],
			userEmail: '', // Add userEmail property
		};
	},
	methods: {
		async fetchMonthlyTotals() {
			try {
				const auth = useAuthStore();
				const token = auth.token;

				console.log("Token:", token);
				console.log("Selected Month:", this.selectedMonth);

				const response = await axios.get("http://localhost:8080/api/v1/totals-last-year", {
					headers: {
						Authorization: `Bearer ${token}`,
					},
					params: {
						month: this.selectedMonth,
					},
				});

				console.log("Response data:", response.data);

				const responseData = response.data;

				if (Array.isArray(responseData) && responseData.length > 0) {
					this.timeRegistrations = responseData;
				} else {
					console.log("No data found for the selected month.");
					this.timeRegistrations = [];
				}
			} catch (error) {
				console.error("Error fetching monthly totals:", error.response || error.message);
				this.timeRegistrations = []; // Reset to empty in case of an error
			}
		},


		monthName(month) {
			return this.months[month - 1];
		},
		async getTotalTimeForCurrentMonth() {
			try {
				const auth = useAuthStore();
				const token = auth.token;
				console.log("JWT Token:", token);

				const userEmail = this.getUserEmail();

				const response = await axios.get("http://localhost:8080/api/v1/total-time-this-month", {
					headers: {
						Authorization: `Bearer ${token}`,
					},
					params: {
						userEmail: userEmail,
					},
				});

				console.log("Total time for this month:", response.data);
				this.totalTime = response.data;
			} catch (error) {
				console.error("Error fetching total time for this month:", error.response || error.message);
			}
		},
		getUserEmail() {
			return this.userEmail;
		},
		async resetTotalTime() {
			try {
				const auth = useAuthStore();
				const token = auth.token;

				await axios.post("http://localhost:8080/api/v1/reset-total-time", null, {
					headers: {
						Authorization: `Bearer ${token}`,
					},
				});

				console.log("Total time reset successfully.");
				await this.fetchMonthlyTotals();
				await this.getTotalTimeForCurrentMonth();
			} catch (error) {
				console.error("Error resetting total time:", error.response || error.message);
			}
		},
	},
	mounted() {
		this.fetchMonthlyTotals();
		this.getTotalTimeForCurrentMonth();
	},
};
</script>

<style scoped></style>
