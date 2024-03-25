<template>
    <input v-model="searchQuery" @input="filterUsers" placeholder="Search for users.."  />
</template>

<script>
export default {
  props: ['users'],
  data() {
    return {
      searchQuery: '',
    };
  },
  methods: {
    filterUsers() {
      this.$emit('search', this.searchQuery);
    },
  },
};
</script>

<style scoped>
</style>