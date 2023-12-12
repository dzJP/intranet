<template>
	<h1>Landing page</h1>
	<NewsLatest />
	<TimeRegister />
	<MonthlyTimeRegister />
</template>

<script>
import NewsLatest from '@/components/NewsLatest.vue';
import MonthlyTimeRegister from '@/components/MonthlyTimeRegister.vue';
import TimeRegister from '@/components/TimeRegister.vue';

export default {
	name: 'HomeView',
	components: {
		NewsLatest,
		TimeRegister,
		MonthlyTimeRegister
	}
}
</script>
