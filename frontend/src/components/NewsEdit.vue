<template>
    <div>
        <button @click="toggleFormVisibility">Edit News</button>

        <!-- Show the form component only when isFormVisible is true -->
        <div v-if="isFormVisible" class="edit-window">
            <p>Form is rendering</p>
            <form @submit.prevent="submitForm">
                <!-- Dropdown -->
                <label>Select News to Edit</label>
                <select v-model="selectedNewsId" @change="onNewsSelectionChange">
                    <option value="">Select News</option>
                    <option v-for="news in fetchedNewsList" :key="news.id" :value="news.id">{{ news.subject }}</option>
                </select>

                <label>Subject</label>
                <input v-model="editedNews.subject" type="text" required>

                <label>Message</label>
                <textarea v-model="editedNews.message" required></textarea>

                <button type="submit" class="btn btn-primary">Update News</button>
                <button type="button" class="btn btn-primary" @click="deleteNewsItem">Delete News</button>
            </form>
        </div>
    </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import { useNewsStore } from '@/stores/news';

export default {
    props: {
        editingNews: Object,
        isEditing: Boolean,
    },
    setup(props) {
        const editedNews = ref({ ...props.editingNews } || {});
        const selectedNewsId = ref(props.editingNews?.id || null);
        const fetchedNewsList = ref([]);
        const isFormVisible = ref(false);

        const newsStore = useNewsStore();

        const fetchNewsList = async () => {
            try {
                const response = await newsStore.getAllNews();
                fetchedNewsList.value = response.data || [];
            } catch (error) {
                console.error('Error fetching news list:', error);
            }
        };
        const submitForm = async () => {
            try {
                console.log('Submitting form with updated news:', editedNews.value);

                const response = await newsStore.updateNews(editedNews.value);

                console.log('Update news response:', response);
                await fetchNewsList();
            } catch (error) {
                console.error('Error updating news:', error);
            }
        };
        const onNewsSelectionChange = () => {
            if (fetchedNewsList.value) {
                const selectedNews = fetchedNewsList.value.find(news => news.id === selectedNewsId.value);
                if (selectedNews) {
                    editedNews.value = { ...selectedNews };
                }
            }
        };
        const toggleFormVisibility = async () => {
            try {

                await fetchNewsList();


                isFormVisible.value = !isFormVisible.value;
            } catch (error) {
                console.error('Error fetching news list:', error);
            }
        };

        const deleteNewsItem = async () => {
            try {
                const newsId = editedNews.value.id;
                console.log('Deleting news with ID:', newsId);
                await newsStore.deleteNews(newsId);

                isFormVisible.value = false;
                await fetchNewsList();

            } catch (error) {
                console.error('Error deleting news:', error);
            }
        };

        onMounted(async () => {
            await fetchNewsList();
        });

        watch(() => props.editingNews, (newVal) => {
            editedNews.value = { ...newVal } || {};
            selectedNewsId.value = newVal?.id || null;
        });

        return {
            editedNews,
            selectedNewsId,
            fetchedNewsList,
            submitForm,
            onNewsSelectionChange,
            isFormVisible,
            toggleFormVisibility,
            deleteNewsItem,
        };
    },
};
</script>