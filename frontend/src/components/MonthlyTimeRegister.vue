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
					{{ registration.date }} - {{ registration.totalTime }} hours
					({{ registration.email }})
				</li>
			</ul>
		</div>
		<div v-else>
			<p>No time registrations found for {{ monthName(selectedMonth) }}.</p>
		</div>

		<!-- <button @click="calculateMonthlyTime">Calculate Monthly Time</button> -->
		<!-- <button @click="resetMonthlyTime">Reset Monthly Time</button> -->
		<button @click="saveAndResetMonthlyTime">Save and reset</button>
	</div>
</template>

<script>
import axios from "axios";
import { useAuthStore } from "@/stores/auth";

export default {
	data() {
		return {
			selectedMonth: new Date().getMonth() + 1,
			timeRegistrations: [],
			months: [
				"January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December"
			],
		};
	},
	methods: {
		async fetchMonthlyTotals() {
			try {
				const auth = useAuthStore();
				const token = auth.token;

				const response = await axios.get("http://localhost:8080/api/v1/totals-last-year", {
					headers: {
						Authorization: `Bearer ${token}`,
					},
					params: {
						month: this.selectedMonth,
						userEmail: auth.user,
					},
				});

				console.log("Response data:", response.data);

				const responseData = response.data;

				if (Array.isArray(responseData) && responseData.length > 0) {
					// Clear the timeRegistrations array and then push the new data
					this.timeRegistrations.splice(0, this.timeRegistrations.length, ...responseData);
				} else {
					console.log("No data found for the selected month.");
					this.timeRegistrations = [];
				}
			} catch (error) {
				console.error("Error fetching monthly totals:", error.response || error.message);
				this.timeRegistrations = [];
			}
		},
		monthName(month) {
			return this.months[month - 1];
		},
		async calculateMonthlyTime() {
			try {
				const auth = useAuthStore();
				const token = auth.token;

				// Make a request to the backend endpoint that triggers the monthly time calculation
				await axios.post("http://localhost:8080/api/v1/calculate-monthly-time", null, {
					headers: {
						Authorization: `Bearer ${token}`,
					},
				});

				console.log("Monthly time calculation triggered. Waiting for backend...");

				// Wait for a few seconds (adjust the duration as needed)
				await new Promise(resolve => setTimeout(resolve, 5000));

				// Fetch the updated monthly totals after the calculation
				await this.fetchMonthlyTotals();
			} catch (error) {
				console.error("Error triggering monthly time calculation:", error.response || error.message);
			}
		},
		async resetMonthlyTime() {
			try {
				const auth = useAuthStore();
				const token = auth.token;

				// Make a request to the backend endpoint that resets monthly time
				await axios.post("http://localhost:8080/api/v1/reset-monthly-time", null, {
					headers: {
						Authorization: `Bearer ${token}`,
					},
				});

				console.log("Monthly time reset triggered. Waiting for backend...");

				// Wait for a few seconds (adjust the duration as needed)
				await new Promise(resolve => setTimeout(resolve, 5000));

				// Fetch the updated monthly totals after the reset
				await this.fetchMonthlyTotals();
			} catch (error) {
				console.error("Error triggering monthly time reset:", error.response || error.message);
			}
		},
		// For testing only
		async saveAndResetMonthlyTime() {
			try {
				const auth = useAuthStore();
				const token = auth.token;

				// Make a request to the backend endpoint that resets monthly time
				await axios.post("http://localhost:8080/api/v1/save-and-reset-monthly-time", null, {
					headers: {
						Authorization: `Bearer ${token}`,
					},
				});

				console.log("Monthly time save and reset triggered. Waiting for backend...");

				// Fetch the updated monthly totals after the reset
				await this.fetchMonthlyTotals();
			} catch (error) {
				console.error("Error triggering monthly time save and reset:", error.response || error.message);
			}
		},
	},
	mounted() {
		this.fetchMonthlyTotals();
	},
};
</script>

<style scoped></style>