<template>
  <div>
    <b-card title="All Appliances" class="border-0 bg-transparent">
      <b-card no-body>
        <b-table
          striped
          hover
          show-empty
          :items="items"
          @row-clicked="goTo"
        ></b-table>
      </b-card>
    </b-card>
  </div>
</template>

<script>
import Axios from 'axios';
import { mapGetters } from 'vuex';
import { API_HOUSEHOLDS } from '../../endpoints';
import { GET_HOUSEHOLD } from '../../store/modules/current_household.methods';

export default {
  name: 'Appliances',
  data() {
    return {
      fields: ['name', 'current_power_usage', 'average_power_usage'],
      items: [],
    };
  },
  methods: {
    goTo(record) {
      this.$router.push({ name: 'Appliance', params: { id: record.id } });
    },
  },
  mounted() {
    Axios.get(`${API_HOUSEHOLDS}/${this.household.id}/appliances`)
      .then((response) => {
        this.items = response.data.map(
          item => ({
            id: item.key.id,
            name: item.name,
            average_power_usage: item.averagePowerUsage,
            current_power_usage: item.currentPowerUsage,
          }),
        );
      }).catch((error) => {
        console.log(error);
        this.$bus.$emit('backendOffline');
      });
  },
  computed: {
    ...mapGetters({
      household: GET_HOUSEHOLD,
    }),
  },
};
</script>

<style scoped>

</style>
