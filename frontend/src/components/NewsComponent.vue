<template>
    <div class="card news-list">
        <div class="card-body px-0 pt-0 pb-2">
            <div class="table-responsive p-0">
                <table class="table mb-0">
                    <thead>
                        <tr>
                            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Subject</th>
                            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Message</th>
                            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                                Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-if="currentNews" :key="currentNews.id">
                            <td>{{ currentNews.subject }}</td>
                            <td>{{ currentNews.message }}</td>
                            <td>
                                <button class="btn btn-edit" @click="editNews(currentNews)">Edit</button>
                                <button class="btn btn-delete" @click="deleteNews(currentNews)">Delete (ID: {{
                                    currentNews.id }})</button>
                            </td>
                        </tr>
                        <tr v-else>
                            <td colspan="3">No news available</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="card-body px-0 pt-0 pb-2">
            <button @click="navigate(-1)" :disabled="currentIndex === 0">Previous</button>
            <button @click="navigate(1)" :disabled="currentIndex === newsList.length - 1">Next</button>
            <form @submit.prevent="createNews">
                <label>Subject</label>
                <input v-model="newNews.subject" type="text" required>
                <label>Message</label>
                <textarea v-model="newNews.message" required></textarea>
                <button type="submit" class="btn btn-primary">Create News</button>
            </form>
        </div>
    </div>

    <div v-if="isEditing" class="edit-window">
        <form @submit.prevent="updateNews">
            <label>Subject</label>
            <input v-model="editingNews.subject" type="text" required>
            <label>Message</label>
            <textarea v-model="editingNews.message" required></textarea>
            <button type="submit" class="btn btn-primary">Update News</button>
            <button @click="cancelEdit" class="btn btn-secondary">Cancel</button>
        </form>
    </div>
</template>

<script>
import { onMounted, ref } from 'vue';
import { useNewsStore } from '@/stores/news';

export default {
    setup() {
        const newsStore = useNewsStore();
        const currentNews = ref(null);
        const newNews = ref({ subject: '', message: '' });
        const editingNews = ref({ id: null, subject: '', message: '' });
        const isEditing = ref(false);
        const currentIndex = ref(0);
        const newsList = ref([]);

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

        const createNews = async () => {
            try {
                const createdNews = await newsStore.createNews(newNews.value);

                console.log("text", createdNews.data);
                newNews.value = { subject: '', message: '', id: createdNews?.id || null };
                getNews();
            } catch (error) {
                console.error('Error creating news:', error);
            }
        };

        const deleteNews = async (news) => {
            console.log(news.id);
            try {
                const newsId = news.id;

                console.log('Deleting news with ID:', newsId);
                await newsStore.deleteNews(newsId);
                getNews();
            } catch (error) {
                console.error('Error deleting news:', error);
            }
        };

        const editNews = (news) => {
            editingNews.value = { ...news };
            isEditing.value = true;
        };

        const updateNews = async () => {
            try {
                await newsStore.updateNews(editingNews.value);
                isEditing.value = false;
                getNews();
            } catch (error) {
                console.error('Error updating news:', error);
            }
        };

        const cancelEdit = () => {
            isEditing.value = false;
        };

        const navigate = (step) => {
            console.log('Before Navigation - currentIndex:', currentIndex.value, 'newsList:', [...newsList.value]);

            const newIndex = currentIndex.value + step;

            if (newIndex >= 0 && newIndex < newsList.value.length) {
                currentIndex.value = newIndex;
                currentNews.value = { ...newsList.value[newIndex], id: newsList.value[newIndex].id || null };
            }

            console.log('After Navigation - currentIndex:', currentIndex.value, 'newsList:', [...newsList.value]);
        };

        onMounted(async () => {
            await getNews();
            currentIndex.value = 0;
        });

        return {
            newsList,
            currentNews,
            newNews,
            createNews,
            deleteNews,
            editingNews,
            isEditing,
            editNews,
            updateNews,
            cancelEdit,
            navigate,
            getNews,
            currentIndex
        };
    },
};
</script>

<style scoped>
.edit-window {
    margin-top: 20px;
    padding: 10px;
    border: 1px solid #ccc;
}
</style>

<style scoped>
.edit-window {
    margin-top: 20px;
    padding: 10px;
    border: 1px solid #ccc;
}
</style>