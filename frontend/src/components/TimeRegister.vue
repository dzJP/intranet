<template>
	<div>
		<h2>Register time</h2>

		<label for="email">Email:</label>
		<input type="text" id="email" v-model="email" />

		<label for="workHours">Work Hours:</label>
		<input type="text" id="workHours" v-model="workHours" />

		<label for="date">Date:</label>
		<input type="date" id="date" v-model="date" />

		<button @click="registerTime">Register Time</button>

		<div v-if="timeRegistrations.length > 0">
			<h3>Time Registrations:</h3>
			<ul>
				<li v-for="registration in timeRegistrations" :key="registration.id">
					{{ registration.date }} - {{ registration.workHours }} hours
					({{ registration.email }})
				</li>
			</ul>
		</div>
		<div v-else>
			<p>No time registrations found.</p>
		</div>
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
			timeRegistrations: [],
		};
	},
	methods: {
		async registerTime() {
			try {
				const auth = useAuthStore();
				const token = auth.token;

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

				this.getTimeRegistrations();
			} catch (error) {
				console.error("Error registering time:", error.response || error.message);
			}
		},
		async getTimeRegistrations() {
			try {
				const auth = useAuthStore();
				const token = auth.token;

				const response = await axios.get("http://localhost:8080/api/v1/time-registrations", {
					headers: {
						Authorization: `Bearer ${token}`,
					},
					params: {
						email: this.email,
					},
				});

				console.log("Time registrations fetched successfully:", response.data);

				this.timeRegistrations = response.data;
			} catch (error) {
				console.error("Error fetching time registrations:", error.response || error.message);
			}
		},
	},
	mounted() {
		this.getTimeRegistrations();
	},
};
</script>
