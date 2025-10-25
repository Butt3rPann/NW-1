<script setup>
import ErrorMessage from '@/components/elements/ErrorMessage.vue';
import productNotFound from '@/assets/images/product-not-found.png'

</script>
 
<template>
<div>
    <ErrorMessage title="sale item" description="The page you're trying to access doesn't exist or has been removed." backPathName="SaleItems" :img="productNotFound">
        <span>Page</span><br/>
        <span>Not Found</span>
    </ErrorMessage>
</div>
</template>
 
<style scoped>

</style>