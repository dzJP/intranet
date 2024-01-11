<template>
    <div>
        <button @click="navigate(-1)" :disabled="currentIndex === 0">Previous</button>
        <span>{{ currentIndex + 1 }} / {{ newsListLength }}</span>
        <button @click="navigate(1)" :disabled="currentIndex === newsListLength - 1">Next</button>
    </div>
</template>

<script>
export default {
    props: {
        currentIndex: Number,
        newsListLength: Number,
        updateIndex: Function,
    },
    methods: {
        navigate(step) {
            const newIndex = this.currentIndex + step;

            if (newIndex >= 0 && newIndex < this.newsListLength) {
                this.updateIndex(newIndex); // Call the 'updateIndex' method from the parent
            } else if (newIndex >= this.newsListLength) {
                // If we reached the end, loop back to the first news
                this.updateIndex(0);
            } else if (newIndex < 0) {
                // If we reached the beginning, loop to the last news
                this.updateIndex(this.newsListLength - 1);
            }
        },
    },
};
</script>