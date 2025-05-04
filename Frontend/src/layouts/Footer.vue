<script setup>
import pinIcon from '@/assets/images/pin.png'
import phoneIcon from '@/assets/images/phone.png'
import mailIcon from '@/assets/images/mail.png'
import facebookIcon from '@/assets/images/facebookLogo.png'
import twitterIcon from '@/assets/images/twitterLogo.png'
import igIcon from '@/assets/images/igLogo.png'
import youtubeIcon from '@/assets/images/youtubeLogo.png'
const footerSections = [
    {
        title: 'Shop',
        items: ['All Products', 'Promotions', 'New Arrivals']
    },
    {
        title: 'Account',
        items: ['My Account', 'Order History', 'Favorites']
    },
    {
        title: 'Help',
        items: ['Delivery', 'Returns', 'Secure Payments', 'Track an Order', 'FAQ']
    },
    {
        title: 'About Us',
        items: ['Careers', 'Terms & Conditions of Sales', 'Privacy Policy', 'Responsibility']
    }
]

const contactInfo = [
    {
        icon: pinIcon,
        text: '126 ถนนประชาอุทิศ แขวงบางมด เขตทุ่งครุ กรุงเทพฯ 10140'
    },
    {
        icon: phoneIcon,
        text: '02-123-4567'
    },
    {
        icon: mailIcon,
        text: 'support@itbmshop.com'
    }
]

const socialIcons = [facebookIcon, twitterIcon, igIcon, youtubeIcon]
</script>

<template>
    <div class="font-rubik bg-[#6F879C] text-[#F0EDEC] h-[28vw] pt-[3vw] pb-[3vw] px-[2vw]">
        <div class="flex justify-between pb-[3vw] border-b-[0.13vw] border-[#F0EDEC]  max-w-[90vw] mx-auto">
            <div v-for="section in footerSections" :key="section.title">
                <p class="font-bold text-[1.6vw] mb-[0.5vw]">{{ section.title }}</p>
                <template v-for="item in section.items" :key="item">
                    <router-link v-if="item === 'All Products'" to="/sale-items"
                        class="text-[1.1vw] font-normal mb-[0.3vw] hover:text-[#334362] cursor-pointer transition-colors duration-150 block">
                        {{ item }}
                    </router-link>
                    <p v-else
                        class="text-[1.1vw] font-normal mb-[0.3vw] hover:text-[#334362] cursor-pointer transition-colors duration-150">
                        {{ item }}
                    </p>
                </template>
            </div>

            <div class="w-[18vw]">
                <p class="font-bold text-[1.6vw] mb-[0.5vw]">Contact Us</p>
                <div v-for="(contact, index) in contactInfo" :key="index" class="flex items-center mb-[0.4vw]">
                    <img :src="contact.icon" alt="" class="mr-[1vw] w-[1.8vw]" />
                    <p class="text-[1vw] font-light">{{ contact.text }}</p>
                </div>
                <div class="flex items-center">
                    <img v-for="(icon, index) in socialIcons" :key="index" :src="icon" alt=""
                        class="mr-[1vw] mt-[0.8vw] w-[3vw]" />
                </div>
            </div>
        </div>
        <div class="flex justify-between items-center mt-[1.5vw] ml-[3vw] mr-[3vw]">
            <div class="flex items-center">
                <img src="@/assets/images/copyright.png" alt="copyright"></img>
                <p class="text-[1.1vw] font-light ml-[0.3vw]">2025 ITB-MShop. All rights reserved.</p>
            </div>
            <div class="flex items-center">
                <p class="text-[1.1vw] font-light mr-[7vw]">Privacy Policy</p>
                <p class="text-[1.1vw] font-light">Terms & Conditions</p>
            </div>
        </div>
    </div>

</template>

<style scoped></style>