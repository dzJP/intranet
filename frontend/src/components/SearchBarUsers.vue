<template>
    <input v-model="searchQuery" @input="filterUsers" placeholder="search employee" class="search-input" />
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

.search-input {
  width: 980px;
  padding: 13px 0 13px 55px;
  background-color: var(--blue);
  color: var(--white);
  border: none;
  border-radius: 50px;
  outline: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='white' class='bi bi-search' viewBox='0 0 16 16'%3E%3Cpath d='M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: 20px center;
  background-size: 20px;
  font-size: 16px;
}

.search-input::placeholder {
  color: var(--white);
}

</style>