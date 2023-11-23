<template>
    <div>
        <h1>Colleagues View</h1>
        <UserList  />
    </div>
</template>

<script>
import UserList from "@/components/UserList.vue";

export default {
    components: {
        UserList,
    },
};
</script>
