<script setup>
import Navbar from '@/layouts/Navbar.vue'
import Footer from '@/layouts/Footer.vue'
import AddSaleItemForm from './components/form/AddSaleItemForm.vue';
</script>
 
<template>
<div>
    <Navbar />
    <AddSaleItemForm></AddSaleItemForm>
    <router-view />
    <Footer />
</div>
</template>
 
<style scoped>

</style>
