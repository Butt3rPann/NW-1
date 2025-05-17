<script setup>
defineProps({
    items: {
        type: Array,
        required: true
    },
    view: {
        type: String,
        default: 'gallery',
        validator: (value) => ['gallery', 'list'].includes(value)
    }
})
</script>

<template>
    <div>
        <template v-if="view === 'list'">
            <div class="flex font-bold text-center bg-[#F9F5F5] justify-center items-center h-15 gap-2 border-[#CFC8BE]" :class="items.length ? 'border-b' : 'border-b-2'">
                <slot name="header" />
            </div>
        </template>
        
        <div :class="view === 'gallery' ? 'grid grid-cols-5 gap-5 mx-auto' : 'flex flex-col'">
            <div v-for="item in items" :key="item.id" class="itbms-row"
                :class="view === 'gallery' 
                    ? 'shadow-[0_0.065rem_0.18rem_0_rgba(0,0,0,0.15)] rounded-md overflow-hidden hover:shadow-[0_0.08rem_0.4rem_rgba(0,0,0,0.15)] hover:scale-[1.01] bg-white' 
                    : 'flex bg-[#FDFDFD] text-center justify-center items-center h-18 border-t border-[#CFC8BE] gap-2'">

                <template v-if="view === 'gallery'">
                    <router-link :to="{ name: 'SaleItemsDetail', params: { saleItemId: item.id } }">
                        <div class="flex items-center justify-center bg-[#FAF6F5]">
                            <img :src="'/nw1/saleItemImage/demoImg1.png'" class="h-[7.5rem] my-8" />
                        </div>
                        <div class="flex bg-white">
                            <slot name="saleItem" :itemInList="item"/>
                        </div>
                    </router-link>
                </template>

                <template v-else>
                    <slot name="item" :itemInList="item"/>
                </template>
            </div>
        </div>
    </div>
</template>

<style scoped></style>