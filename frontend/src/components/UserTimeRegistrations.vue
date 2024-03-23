<template>
  <div class="table-container">

    <div class="select-wrapper">
      <label for="month">Select Month:</label>
      <select v-model="selectedMonth" @change="fetchUsersTotalTime" class="select-month">
        <option v-for="(month, index) in months" :value="index + 1" :key="index">{{ month }}</option>
      </select>
    </div>

    <table class="user-table">
      <thead>
        <tr>
          <th>Employee</th>
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
              <li v-for="(totalTime, projectId) in user.totalPerProject" :key="projectId">
                <span v-if="projects[projectId]">{{ projects[projectId].project }}: </span>{{ totalTime }}
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
import { useProjectStore } from '../stores/project';

export default defineComponent({
  setup() {
    const timeStore = useTimeStore();
    const usersTotalTime = ref([]);
    const projectStore = useProjectStore();
    const projects = ref({});
    const selectedMonth = ref(new Date().getMonth() + 1);
    const months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    const fetchUsersTotalTime = async () => {
      const year = new Date().getFullYear();
      await timeStore.getUsersTotalTimePerMonth(year, selectedMonth.value);
      usersTotalTime.value = timeStore.usersTotalTime;
    };

    const fetchProjects = async () => {
      const allProjects = await projectStore.getAllProjects(); allProjects.forEach(project => {
        projects.value[project.id] = project;
      });
      projects.value = { ...projects.value };
    };

    onMounted(async () => {
      await fetchUsersTotalTime();
      await fetchProjects();
    });

    return {
      usersTotalTime,
      selectedMonth,
      months,
      fetchUsersTotalTime,
      projects,
    };
  },
});
</script>

<style scoped>

.table-container, .table-container th, .table-container td, .select-wrapper, .select-wrapper label, .select-month, .select-month option {
  font-family: 'Oxanium', sans-serif;
}

.table-container {
  margin: 20px auto;
  color: var(--white);
  padding: 20px;
  width: 980px;
  background-color: var(--dark-blue);
  border: 2px solid #111C44;
  position: relative;
  font-size: 20px;
}

.select-wrapper {
  position: absolute;
  top: 10px;
  right: 30px;
}

.table-container th,
.table-container td {
  font-size: 16px;
}

.table-container table {
  margin-top: 30px;
  width: 100%;
}

.table-container select {
  margin-left: 5px;
}

.table-container ul {
  list-style: none;
  padding: 0;
}

</style>