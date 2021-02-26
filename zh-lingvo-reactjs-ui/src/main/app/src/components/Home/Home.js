import React from 'react';
import { useSelector } from 'react-redux';

const Home = () => {
    const username = useSelector(store => store.auth.username);

    const greeting = username ? `Welcome, ${username}!` : 'Welcome!';
    return <h1>{greeting}</h1>;
};

export default Home;
