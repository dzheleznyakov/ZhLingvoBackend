import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import axios from '../../axios-api';

const Home = () => {
    const username = useSelector(store => store.auth.username);
    const [languages, setLanguages] = useState([]);

    const loadLanguages = (setLanguages) => {
        axios.get('/languages')
            .then(res => res.data)
            .then(languages => setLanguages(languages));
    }

    useEffect(() => {
        loadLanguages(setLanguages);
    }, [setLanguages]);

    const greeting = username ? `Welcome, ${username}!` : 'Welcome!';
    return (
        <div>
            <h1>{greeting}</h1>
            <button onClick={() => loadLanguages(setLanguages)}>Reload languages</button>
            <hr />
            <div>The following languages are available:</div>
            <ul>
                {languages.map(lang => <li key={lang}>{lang}</li>)}
            </ul>
        </div>
    );
};

export default Home;
