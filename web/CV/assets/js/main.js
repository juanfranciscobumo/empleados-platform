// Main JS File

// Fixed Header on Scroll
const header = document.getElementById('site-header');
const fixedHeader = () => {
    if (window.scrollY > 100) {
        header.classList.add('ds-fixed-header');
    } else {
        header.classList.remove('ds-fixed-header');
    }
};

// Add scroll event listener
window.addEventListener('scroll', fixedHeader);

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    // Apply active state to language switcher
    const languageSwitchers = document.querySelectorAll('.ds-language-switcher .ds-button');
    
    // Add animation classes to elements on scroll
    const animateOnScroll = () => {
        const animatedElements = document.querySelectorAll('.ds-experience-list article, .ds-work-list, .ds-skills-list li, .skills-section h3');
        
        animatedElements.forEach(element => {
            const elementPosition = element.getBoundingClientRect().top;
            const screenPosition = window.innerHeight / 1.2;
            
            if (elementPosition < screenPosition) {
                if (element.classList.contains('ds-work-list')) {
                    element.classList.add('slide-up');
                } else if (element.classList.contains('ds-skills-list li')) {
                    setTimeout(() => {
                        element.classList.add('fade-in');
                    }, Math.random() * 500);
                } else {
                    element.classList.add('fade-in');
                }
            }
        });
    };
    
    // Run on load
    animateOnScroll();
    
    // Add event listener for scroll
    window.addEventListener('scroll', animateOnScroll);
    
    // Add smooth scroll to anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            if (targetId === '#') return;
            
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                targetElement.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });
});