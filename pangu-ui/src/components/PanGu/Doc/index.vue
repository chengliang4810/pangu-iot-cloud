<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script>
export default {
  name: 'PanGu-Cloud-Plus-Wiki',
  data() {
    return {
      url: 'https://gitee.com/JavaLionLi/PanGu-Cloud-Plus/wikis'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
