<template>
    <div>
        <h1>Colleagues View</h1>
        <UserList :apiUrl="apiUrl" :searchQuery="searchQuery" />
    </div>
</template>

<script>
import UserList from "@/components/UserList.vue";

export default {
    components: {
        UserList,
    },
    data() {
        return {
            apiUrl: "http://localhost:8080/api/v1/colleagues",
            searchQuery: "",
        };
    },
};
</script>
