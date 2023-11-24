<template>
	<h1>This is the home page</h1>
	<TimeRegister />
</template>

<script>
import TimeRegister from '@/components/TimeRegister.vue';

export default {
	name: 'HomeView',
	components: {
		TimeRegister,
	}
}
</script>
