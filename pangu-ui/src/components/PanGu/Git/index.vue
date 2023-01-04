<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'PanGu-Cloud-Plus-Git',
  data() {
    return {
      url: 'https://gitee.com/JavaLionLi/PanGu-Cloud-Plus'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
