<template>
    <div class="card-body px-0 pt-0 pb-2">
        <div v-if="newsList && newsList.length > 0">
            <table class="table mb-0">
                <thead>
                    <tr>
                        <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Subject</th>
                        <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Message</th>
                        <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Actions
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="newsItem in newsList" :key="newsItem ? newsItem.id : null">
                        <td>{{ newsItem ? newsItem.subject || '' : '' }}</td>
                        <td>{{ newsItem ? newsItem.message || '' : '' }}</td>
                        <td>
                            <button @click="editNewsItem(newsItem)">Edit</button>
                            <button @click="deleteNewsItem(newsItem)">Delete</button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        newsList: Array,
        editNews: Function,
        deleteNews: Function,
    },
    methods: {
        editNewsItem(newsItem) {
            if (newsItem && newsItem.id !== null) {
                this.$emit('edit-news', newsItem);
            }
        },
        deleteNewsItem(newsItem) {
            if (newsItem && newsItem.id !== null) {
                this.$emit('delete-news', newsItem);
            }
        },
    },
};
</script>