<template>
    <input v-model="searchQuery" @input="handleInput" placeholder="Sök">
</template>

<script>
export default {
    data() {
        return {
            searchQuery: '',
        };
    },
    methods: {
        handleInput() {
            this.$emit('search', this.searchQuery);
        },
    },
};
</script>

<style scoped>
.search-input {
    margin-bottom: 20px;
}
</style>