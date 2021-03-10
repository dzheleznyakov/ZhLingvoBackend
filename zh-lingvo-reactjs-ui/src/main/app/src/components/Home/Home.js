import React from 'react';
import { Link } from 'react-router-dom';

import { AUTH } from '../../static/constants/paths';
import { useBreadcrumbs } from '../../hooks';

const Home = () => {
    useBreadcrumbs('Home');

    return (
        <div>
            <h1>Welcome!</h1>
            <br />
            <p>Please <Link to={AUTH}>log in</Link> to continue.</p>
        </div>
    );
};

// const Home = () => {
//     const username = useSelector(store => store.auth.username);
//     const dispatch = useDispatch();
//     const [languages, setLanguages] = useState([]);

//     const loadLanguages = (setLanguages) => {
//         axios.get('/languages')
//             .then(res => res.data)
//             .then(languages => setLanguages(languages));
//     }

//     useEffect(() => {
//         const breadcrumbs = username ? [username] : ['Home'];
//         dispatch(actions.setBreadcrumbs(breadcrumbs));
//     }, [dispatch, username]);

//     useEffect(() => {
//         if (username !== null)
//             loadLanguages(setLanguages);
//     }, [setLanguages, username]);

//     const greeting = username ? `Welcome, ${username}!` : 'Welcome!';
//     const languagesList = username && (
//         <Fragment>
//             <div>The following languages are available:</div>
//             <ul>
//                 {languages.map(lang => <li key={lang}>{lang}</li>)}
//             </ul>
//         </Fragment>
//     );

//     return (
//         <div>
//             <h1>{greeting}</h1>
//             {username && <button onClick={() => loadLanguages(setLanguages)}>Reload languages</button>}
//             <hr />
//             {languagesList}
//         </div>
//     );
// };

export default Home;
