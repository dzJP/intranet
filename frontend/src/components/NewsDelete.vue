<template>
    <div>
        <button class="btn btn-delete" @click="deleteNewsItem" v-if="newsItem && newsItem.id">
            Delete - ID: {{ newsItem.id }}
        </button>
    </div>
</template>


<script>
import { useNewsStore } from '@/stores/news';


export default {
    props: {
        newsItem: {
            type: Object,
            required: true,
        },
    },
    setup(props, { emit }) {
        const newsStore = useNewsStore();

        const deleteNewsItem = async () => {
            try {
                const newsId = props.newsItem.id;
                console.log('Deleting news with ID:', newsId);
                await newsStore.deleteNews(newsId);

                // Emit an event to notify the parent component about the deletion
                // You can pass additional data if needed
                emit('news-deleted', newsId);
            } catch (error) {
                console.error('Error deleting news:', error);
            }
        };

        return {
            deleteNewsItem,
        };
    },
};
</script>