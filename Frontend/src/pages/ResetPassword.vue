<script setup>
import ChangePasswordForm from '@/components/form/ChangePasswordForm.vue';
</script>
 
<template>
    <ChangePasswordForm form="reset" :reset-token="$route.query.token"/>
</template>
 
<style scoped>

</style>