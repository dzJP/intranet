<template>
	<h1>Landing page</h1>
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
