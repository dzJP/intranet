<template>
    <div class="card news-list">
        <NewsTable :newsList="showAllNews ? newsList : [currentNews]" :currentNews="currentNews" @edit-news="editNewsItem"
            @delete-news="deleteNewsItem" />
        <div class="card-body news px-0 pt-0 pb-2">
            <NewsNavigation v-if="!showAllNews && newsList.length > 1" :currentIndex="currentIndex"
                :newsListLength="newsList.length" :updateIndex="updateCurrentIndex" />

            <button @click="toggleShowAllNews">
                {{ showAllNews ? 'View latest news' : 'View all news' }}
            </button>
            <NewsEditForm v-if="isEditing" :editingNews="editingNews" @update-news="handleUpdateNews"
                @cancel-edit="cancelEdit" />
            <NewsDelete :newsItem="deletingNews" @news-deleted="handleNewsDeleted" />
            <NewsCreate @news-created="handleNewsCreated" />
        </div>
    </div>
</template>

<script>
import { onMounted, ref, watch } from 'vue';
import { useNewsStore } from '@/stores/news';
import NewsCreate from '@/components/NewsCreate.vue';
import NewsTable from '@/components/NewsTable.vue';
import NewsNavigation from '@/components/NewsNavigation.vue';
import NewsEditForm from '@/components/NewsEditForm.vue';
import NewsDelete from '@/components/NewsDelete.vue';

export default {
    components: { NewsCreate, NewsDelete, NewsTable, NewsNavigation, NewsEditForm },
    setup() {
        const newsStore = useNewsStore();
        const currentNews = ref(null);
        const editingNews = ref({ id: null, subject: '', message: '', date: '', deadline: '' });
        const isEditing = ref(false);
        const currentIndex = ref(0);
        const newsList = ref([]);
        const showAllNews = ref(false);
        const deletingNews = ref(null);

        const getNews = async () => {
            try {
                const response = await newsStore.getAllNews();
                console.log('Fetched news data:', response.data);

                newsList.value = response.data.map(news => ({ ...news, id: news.id || null }));
                currentIndex.value = 0;
                currentNews.value = newsList.value.length > 0 ? { ...newsList.value[0] } : null;
            } catch (error) {
                console.error('Error fetching news:', error);
            }
        };

        const updateCurrentIndex = newIndex => {
            currentIndex.value = newIndex;
            currentNews.value = { ...newsList.value[newIndex], id: newsList.value[newIndex].id || null };
        };

        onMounted(async () => {
            try {
                await getNews();
            } catch (error) {
                console.error('Error initializing component:', error);
            }
        });

        // Log values for debugging
        watch([isEditing, editingNews], ([newIsEditing, newEditingNews]) => {
            console.log('isEditing:', newIsEditing);
            console.log('editingNews:', newEditingNews);
        });

        const editNewsItem = news => {
            editingNews.value = { ...news };
            isEditing.value = true;
        };

        const updateNews = async () => {
            try {
                await newsStore.updateNews(editingNews.value);
                isEditing.value = false;
                console.log('Updated news:', editingNews.value);
                await getNews();
            } catch (error) {
                console.error('Error updating news:', error);
            }
        };

        const handleUpdateNews = updatedNews => {
            console.log('Received updated news:', updatedNews);

            const updatedIndex = newsList.value.findIndex(news => news.id === updatedNews.id);

            if (updatedIndex !== -1) {
                newsList.value[updatedIndex] = { ...updatedNews };
            }

            isEditing.value = false;
            editingNews.value = null;
            currentNews.value = { ...updatedNews };
        };

        const cancelEdit = () => {
            isEditing.value = false;
            editingNews.value = null;
        };

        const toggleShowAllNews = async () => {
            showAllNews.value = !showAllNews.value;

            if (showAllNews.value) {
                // Load all news when switching to view all news
                await getNews();
            } else {
                // Display the first news article when switching back to view latest news
                currentIndex.value = 0;
                currentNews.value = newsList.value.length > 0 ? { ...newsList.value[0] } : null;
            }
        };

        const deleteNewsItem = async newsItem => {
            deletingNews.value = newsItem;
            await newsStore.deleteNews(newsItem.id);
            deletingNews.value = null;
            await getNews();
        };

        return {
            newsList,
            currentNews,
            showAllNews,
            editingNews,
            isEditing,
            currentIndex,
            handleNewsCreated: getNews,
            updateCurrentIndex,
            editNewsItem,
            updateNews,
            handleUpdateNews,
            cancelEdit,
            toggleShowAllNews,
            deletingNews,
            deleteNewsItem
        };
    },
};
</script>