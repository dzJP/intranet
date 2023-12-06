<template>
	<h1>Landing page</h1>
	<TimeRegister />
	<MonthlyTimeRegister />
</template>

<script>
import MonthlyTimeRegister from '@/components/MonthlyTimeRegister.vue';
import TimeRegister from '@/components/TimeRegister.vue';

export default {
	name: 'HomeView',
	components: {
    TimeRegister,
    MonthlyTimeRegister
}
}
</script>
