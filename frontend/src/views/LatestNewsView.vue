<template>
    <div>
        <h1>Latest news</h1>
        <NewsList />

    </div>
</template>

<script>
import NewsList from '@/components/NewsList.vue'

export default {
	name: "LatestNewsView",
    components: {
        NewsList,
    },

};
</script>


<style>
</style>
