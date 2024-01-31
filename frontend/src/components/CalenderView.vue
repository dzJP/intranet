<template>
  <div class="calendar-wrapper">

    <div class="select-wrapper">
      <select v-model="view">
        <option value="weekly">Weekly</option>
        <option value="monthly">Monthly</option>
      </select>
    </div>

    <div class="content-wrapper">
      <VDatePicker 
      class="custom-calendar" 
      v-model="time.date" 
      :view="view" 
      :min-date="twoMonthsBack" 
      :max-date="today"
      show-weeknumbers 
      trim-weeks borderless 
      :attributes="attributes" 
      @dayclick="handleDateChange" 
      @did-move="handleCalendarMove">

        <template #day-popover>
          <div class="day-popover">
            <label>Time</label>
            <input type="number" v-model="time.workHours">
            <label>Project</label>
            <select v-model="selectedProject">
              <option value="null" disabled>Select Project</option>
              <option v-for="project in activeProjects" :key="project.id" :value="project.id">
                {{ project.project }}
              </option>
            </select>
            <button @click="time.registerTime(selectedProject)" class="save-btn">Save</button>

            <div v-if="selectedDateRegistrations.length > 0">
              <ul>
                <li v-for="registration in selectedDateRegistrations" :key="registration.id">
                  {{ registration.workHours }} tim - {{ getProjectName(registration.projectId) }}

                  <button @click="deleteTimeRegistration(registration.id)" class="delete-btn">Delete</button>

                </li>
              </ul>
            </div>

          </div>
        </template>

      </VDatePicker>

      <div class="time-container">
        <div v-if="time.totalTime !== null">
          <p>Total time worked: <strong>{{ time.totalTime }}h</strong></p>
        </div>

        <div v-if="time.timeRegistrations.length > 0">
          <ul>
            <li v-for="registration in time.timeRegistrations" :key="registration.id">
              {{ registration.date }} - {{ registration.workHours }}h
            </li>
          </ul>
        </div>
      </div>

      <!-- <button @claick="saveAndResetMonthlyTime">Save and reset</button> -->
    </div>
  </div>
</template>
  
<script setup>
import { ref, computed, onMounted } from 'vue';
import { useTimeStore } from '@/stores/time';
import { useProjectStore } from '@/stores/project';
// import { useAuthStore } from '@/stores/auth';
// import axios from 'axios';

const projects = useProjectStore();
const selectedProject = ref(null);

const activeProjects = computed(() => projects.projects.filter(project => project.active));

const time = useTimeStore();

const view = ref('monthly');

const today = new Date();

const twoMonthsBack = new Date(today.getFullYear(), today.getMonth() - 2, 1);

const formatDate = date => {
  if (!(date instanceof Date)) return null;
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

time.date = formatDate(today);

const handleDateChange  = (calendarDay) => {
  time.date = calendarDay.id;
  selectedProject.value = "null";
};

const attributes = computed(() => generateAttributesInRange(twoMonthsBack, today));

const generateAttributesInRange = (start, end) => {
  const attributes = [];
  const registeredDates = time.timeRegistrations.map(registration => registration.date);

  for (let currentDate = new Date(start); currentDate <= end; currentDate.setDate(currentDate.getDate() + 1)) {
    const currentDateFormatted = formatDate(currentDate);
    const isRegistered = registeredDates.includes(currentDateFormatted);
    attributes.push({
      dates: new Date(currentDate),
      popover: {
        placement: 'bottom',
      },
      dot: {
        color: isRegistered ? 'red' : 'transparent',
      },
    });
  }
  return attributes;
};

const handleCalendarMove = (visiblePages) => {
  const calendarData = visiblePages[0];

  if (calendarData) {
    const month = calendarData.month;
    const year = calendarData.year;

    time.getTimeRegistrationsForSelectedMonth(year, month);
    time.getTotalTimeForSelectedMonth(year, month);
  }
};

// For testing only 
// const saveAndResetMonthlyTime = async () => {
//   try {
//     const auth = useAuthStore();
//     const token = auth.token;

//     await axios.post("http://localhost:8080/api/v1/save-and-reset-monthly-time", null, {
//       headers: {
//         Authorization: `Bearer ${token}`,
//       },
//     });

//     console.log("Monthly time saved.");


//   } catch (error) {
//     console.error("Error triggering monthly time save and reset:", error.response || error.message);
//   }
// };

const selectedDateRegistrations = computed(() => {
  const selectedDate = new Date(time.date);
  return time.timeRegistrations.filter(registration => {
    const registrationDate = new Date(registration.date);
    return isSameDay(selectedDate, registrationDate);
  });
});

const isSameDay = (date1, date2) => {
  return (
    date1.getFullYear() === date2.getFullYear() &&
    date1.getMonth() === date2.getMonth() &&
    date1.getDate() === date2.getDate()
  );
};

const getProjectName = (projectId) => {
  const project = projects.projects.find(p => p.id === projectId);
  return project ? project.project : "Unknown Project";
};

const deleteTimeRegistration = async (registrationId) => {
  try {
    await time.deleteTimeRegistration(registrationId);
    console.log('Time registration deleted successfully.');
  } catch (error) {
    console.error('Error deleting time registration:', error);
  }
};

onMounted(() => {
  projects.getAllProjects();
  const today = new Date();
  const year = today.getFullYear();
  const month = today.getMonth() + 1;

  time.getTimeRegistrationsForSelectedMonth(year, month);
  time.getTotalTimeForSelectedMonth(year, month);
});

</script>

<style>
.calendar-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.select-wrapper {
  margin-top: 10px;
  margin-left: 375px;
}

.content-wrapper {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  justify-content: center;
}

.custom-calendar.vc-container {
  width: 800px;
}

.custom-calendar.vc-container .vc-header {
  background-color: #f8fafc;
}

.custom-calendar.vc-container .vc-header .vc-title {
  position: relative;
  color: #868c91;
}

.custom-calendar.vc-container button {
  background-color: transparent;
  border: none;
  cursor: pointer;
  outline: none;
}

.custom-calendar.vc-container .vc-weeks {
  padding: 0px;
}

.custom-calendar.vc-container .vc-weekday {
  background-color: #f8fafc;
  border-bottom: 1px solid #eaeaea;
  border-top: 1px solid #eaeaea;
  padding: 5px 0;
}

.custom-calendar.vc-container .vc-day {
  padding: 0 5px 3px 5px;
  text-align: left;
  min-height: 80px;
  min-width: 80px;
  overflow: auto;
  background-color: white;
}

.custom-calendar.vc-container .vc-day.weekday-1,
.custom-calendar.vc-container .vc-day.weekday-7 {
  background-color: #eff8ff;
}

.custom-calendar.vc-container .vc-day:not(.on-bottom) {
  border-bottom: 1px solid #b8c2cc;
}

.custom-calendar.vc-container .vc-day:not(.on-bottom).weekday-1 {
  border-bottom: 1px solid #b8c2cc;
}

.custom-calendar.vc-container .vc-day:not(.on-right) {
  border-right: 1px solid #b8c2cc;
}

.custom-calendar.vc-container .vc-highlight {
  width: 30px;
  height: 30px;
}

.custom-calendar.vc-container .vc-dots {
  margin-bottom: 15px;
}

.day-popover {
  display: flex;
  flex-direction: column;
  width: 250px;
  padding: 5px;
}

.day-popover ul {
  list-style: none;
  padding: 0;
  font-size: 14px;
  line-height: 0.4;
}

.day-popover .save-btn {
  margin: 8px 0 8px;
  padding: 5px;
  width: 70px;
  background-color: #4b4b4b;
  color: #fff;
  border-radius: 5px;
  border: 1px solid #2e2d2d;
}

.day-popover .delete-btn {
  margin: 3px 0 3px;
  padding: 6px;
  width: 55px;
  background-color: #c6c5c5;
  color:#2e2d2d;
  font-weight: 500;
  border: none;
}

.day-popover .save-btn:hover {
  background-color: #5b5b5b;
}
.day-popover .delete-btn:hover {
  background-color: #c44242;
  color: #fff;
  border-radius: 3px;
}

.time-container {
  margin: 10px 0 0 40px;
  width: 300px;
  padding: 20px;
  border-radius: 1px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
}

.time-container ul {
  list-style: none;
  padding: 0;
}

</style>