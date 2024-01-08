<template>
	<div>
		<h1>Landing page</h1>
		<NewsComponent />
		<TimeRegister />
		<MonthlyTimeRegister />
	</div>
</template>

<script>
import NewsComponent from "@/components/NewsComponent.vue"
import MonthlyTimeRegister from "@/components/MonthlyTimeRegister.vue";
import TimeRegister from "@/components/TimeRegister.vue";

export default {
	name: "HomeView",
	components: {
		NewsComponent,
		TimeRegister,
		MonthlyTimeRegister,
	},
};
</script>
