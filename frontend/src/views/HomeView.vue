<template>
	<div>
		<h1>Landing page</h1>
		<div>
			<h2>Latest News</h2>
			<NewsList :allow-edit="false" :max-entries="1" :hide-search-and-toggle-show-all="true" />
		</div>
		<TimeRegister />
		<MonthlyTimeRegister />
	</div>
</template>

<script>
import NewsList from "@/components/NewsList.vue"
import MonthlyTimeRegister from "@/components/MonthlyTimeRegister.vue";
import TimeRegister from "@/components/TimeRegister.vue";

export default {
	name: "HomeView",
	components: {
		NewsList,
		TimeRegister,
		MonthlyTimeRegister,
	},
};
</script>
