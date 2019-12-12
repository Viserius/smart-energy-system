<template>
  <div>
    <b-card title="All Batteries" class="border-0 bg-transparent">
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
import { GET_HOUSEHOLD } from '../../store/modules/current_household.methods';
import { API_HOUSEHOLDS } from '../../endpoints';

export default {
  name: 'Batteries',
  data() {
    return {
      fields: ['name', 'current_power_storage', 'total_power_storage'],
      items: [],
    };
  },
  methods: {
    goTo(record) {
      this.$router.push({ name: 'Battery', params: { id: record.id } });
    },
  },
  mounted() {
    Axios.get(`${API_HOUSEHOLDS}/${this.household.id}/batteries`)
      .then((response) => {
        this.items = response.data.map(
          item => ({
            id: item.key.id,
            name: item.name,
            current_power_storage: item.currentPowerStorage,
            total_power_storage: item.totalPowerStorage,
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
