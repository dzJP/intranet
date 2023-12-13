<template>
    <div class="calendar-wrapper">

      <select v-model="view">
        <option value="weekly">Weekly</option>
        <option value="monthly">Monthly</option>
      </select>
  
      <VDatePicker
      class="date-picker"
        v-model="time.date"
        :view="view" 
        :min-date="twoMonthsBack"
        :max-date="new Date()"
        show-weeknumbers
        :attributes="attributes"
        @update:model-value="handleDateChange"
      >
        <template #day-popover>
          <div class="popup">
            <label>Registrera tid</label>
            <input type="number" v-model="time.workHours">
            <label>Projekt</label>
            <select>
              <option value="project1">Projekt 1</option>
              <option value="project2">Projekt 2</option>
            </select>
            <button @click="time.registerTime">Spara</button>
          </div>
        </template>

      </VDatePicker>

      <div v-if="time.totalTime !== null">
        <p>Total work hours for this month: {{ time.totalTime }} hours</p>
      </div>
  
      <div v-if="time.timeRegistrations.length > 0">
        <ul>
          <li v-for="registration in time.timeRegistrations" :key="registration.id">
            {{ registration.date }} - {{ registration.workHours }} hours 
          </li>
        </ul>
    </div>
  
      
    <button @click="saveAndResetMonthlyTime">Save and reset</button>
    </div>


  </template>
  
<script setup>
import { ref, computed, onMounted } from 'vue';
import { useTimeStore } from '@/stores/time';
import { useAuthStore } from '@/stores/auth';
import axios from 'axios';

const time = useTimeStore();

const fetchTimeRegistrations = async () => {
  try {
    const auth = useAuthStore();
    await time.getTimeRegistrations(auth.user);
  } catch (error) {
    console.error("Error fetching time registrations:", error.response || error.message);
  }
};

const fetchTotalTimeForCurrentMonth = async () => {
  try {
    const auth = useAuthStore();
    await time.getTotalTimeForCurrentMonth(auth.user);
  } catch (error) {
    console.error("Error fetching total time for this month:", error.response || error.message);
  }
};

const view = ref('monthly');

const today = new Date();
const twoMonthsBack = new Date(today.getFullYear(), today.getMonth() - 2, 1); 

const formatDate = date => {
  if (!date) return null;
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

time.date = formatDate(today);

const handleDateChange = selectedDate => {
    const formattedDate = formatDate(selectedDate);
    if (formattedDate) {
        time.date = formattedDate;
    }
};


const generateTodosInRange = (start, end) => {
  const todos = [];
  const registeredDates = time.timeRegistrations.map(registration => new Date(registration.date));

  for (let currentDate = new Date(start); currentDate <= end; currentDate.setDate(currentDate.getDate() + 1)) {
    const currentDateFormatted = formatDate(currentDate);
    const isRegistered = registeredDates.some(date => formatDate(date) === currentDateFormatted);
    todos.push({
      dates: new Date(currentDate),
      registered: isRegistered,
    });
  }
  return todos;
};

const todosInRange = generateTodosInRange(twoMonthsBack, today);


const attributes = computed(() => [
  ...todosInRange.map(todo => ({
    dates: todo.dates,
    popover: {
      placement: 'right',
    },
    customData: todo,
    visibility: 'click',
  })),
]);


// For testing only 
const saveAndResetMonthlyTime = async () => {
  try {
    const auth = useAuthStore();
    const token = auth.token;

    await axios.post("http://localhost:8080/api/v1/save-and-reset-monthly-time", null, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    console.log("Monthly time saved.");


  } catch (error) {
    console.error("Error triggering monthly time save and reset:", error.response || error.message);
  }
};

onMounted(() => {
  fetchTimeRegistrations();
  fetchTotalTimeForCurrentMonth();
});
fetchTimeRegistrations();
fetchTotalTimeForCurrentMonth();

</script>

<style>

.date-picker  {
    width: 600px;
    background-color: rgba(222, 228, 235, 0.131);
    color: #000;
}

.calendar-wrapper {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.popup {
    width: 250px;
    height: 250px;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.25);
}
</style>