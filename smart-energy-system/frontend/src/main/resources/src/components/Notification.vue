<template>
  <div/>
</template>

<script>
export default {
  name: 'Notification',
  data() {
    return {
      lastTimestamp: 0,
    };
  },
  methods: {
    notifyWebsiteOffline() {
      if (Date.now() - this.lastTimestamp <= 5000) { return; }
      this.$bvToast.toast('Our server is facing difficulties. Please try again later.', {
        title: 'Backend Offline',
        autoHideDelay: 5000,
      });
      this.lastTimestamp = Date.now();
    },
  },
  created() {
    this.$bus.on('backendOffline', this.notifyWebsiteOffline);
  },
};
</script>
