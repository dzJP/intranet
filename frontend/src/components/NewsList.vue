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
                    <tr v-for="newsItem in newsList" :key="newsItem?.id ">
                        <td>{{ newsItem ? newsItem.subject || '' : '' }}</td>
                        <td>{{ newsItem ? newsItem.message || '' : '' }}</td>
                        <td>
                            <button @click="$emit('edit-news', newsItem)">Edit</button>
                            <NewsDelete :newsItem="newsItem" @news-deleted="handleNewsDeleted" @get-news="getNews" />
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import NewsDelete from '@/components/NewsDelete.vue';

export default {
    props: {
        newsList: Array,
    },
    components: {
        NewsDelete,
    },
    methods: {
        async updateNews(newsItem) {
            try {
                console.log('Updated news:', newsItem);
                this.$emit('update-news', newsItem);
            } catch (error) {
                console.error('Error updating news:', error);
            }
        },
        handleNewsDeleted(deletedNewsId) {
            console.log('News deleted with ID:', deletedNewsId);
            this.$emit('get-news');
        },
    },
};
</script>