<template>
    <div class="table-container">
      
  <div>
      <label for="month">Select Month:</label>
      <select v-model="selectedMonth" @change="fetchUsersTotalTime">
        <option v-for="(month, index) in months" :value="index + 1" :key="index">{{ month }}</option>
      </select>
    </div>
  
      <table>
        <thead>
          <tr>
            <th>User</th>
            <th>Total Time</th>
            <th>Total Project</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(user, index) in usersTotalTime" :key="index">
            <td>{{ user.email }}</td>
            <td>{{ user.totalTime }}</td>
            <td>
              <ul>
                <li v-for="(hours, projectId) in user.totalPerProject" :key="projectId">
                  {{ projectId }}: {{  hours }}h
              </li>
              </ul>
            </td>
          </tr>
        </tbody>
      </table>

    </div>
  </template>
  
  <script>
  import { defineComponent, onMounted, ref } from 'vue';
  import { useTimeStore } from '../stores/time';
  
  export default defineComponent({
    setup() {
      const timeStore = useTimeStore();
      const usersTotalTime = ref([]);
      
      const selectedMonth = ref(new Date().getMonth() + 1);
      const months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
  
      const fetchUsersTotalTime = async () => {
        const year = new Date().getFullYear();
        await timeStore.getUsersTotalTimePerMonth(year, selectedMonth.value);
        usersTotalTime.value = timeStore.usersTotalTime;
      };

onMounted(async () => {
      fetchUsersTotalTime();
    });
  
      return {
        usersTotalTime,
        selectedMonth,
        months,
        fetchUsersTotalTime,
      };
    },
  });
  </script>
  
  <style>

  .table-container {
    margin: 20px auto;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  
  }
  
  .table-container table {
    width: 650px;
    padding: 0;
    background-color: aliceblue;
  }
  
  .table-container ul {
    list-style: none;
    padding: 0;
  }
  
  </style>