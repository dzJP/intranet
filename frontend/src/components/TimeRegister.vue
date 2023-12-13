<!-- <template>
	<div>
		<h2>Register time</h2>
		<label for="workHours">Work Hours:</label>
		<input type="number" id="workHours" v-model="time.workHours" />

		<label for="date">Date:</label>
		<input type="date" id="date" v-model="time.date" />

		<button @click="time.registerTime">Register Time</button>

		<div v-if="time.timeRegistrations.length > 0">
			<h3>Time Registrations:</h3>
			<ul>
				<li v-for="registration in time.timeRegistrations" :key="registration.id">
					{{ registration.date }} - {{ registration.workHours }} hours
					({{ registration.email }})
				</li>
			</ul>
		</div>
		<div v-else>
			<p>No time registrations found.</p>
		</div>

		<div v-if="time.totalTime !== null">
			<h3>Total Time This Month</h3>
			<p>Total work hours for this month: {{ time.totalTime }} hours</p>
		</div>

	</div>
</template>
	
<script>
import { useTimeStore } from '@/stores/time';

export default {
	setup() {
		const time = useTimeStore();

		return {
			time,
		};

	},
};
</script> -->