import React from 'react';
import PropTypes from 'prop-types';

import classes from './Meaning.module.scss';

import Remark from './Remark';
import Translation from './Translation';
import Example from './Example';
import { meaningType } from '../wordTypes';

const Meaning = props => {
    const { meaning } = props;
    const translations = (meaning.translations || [])
        .map(tr => <Translation key={tr.id} entry={tr} />);
    const examples = (meaning.examples || [])
        .map(ex => <Example key={ex.id} entry={ex} />);

    return (
        <li className={classes.Meaning}>
            <Remark value={meaning.remark} />
            {translations}
            {examples}
        </li>
    );
};

Meaning.propTypes = {
    meaning: meaningType.isRequired,
};

export default Meaning;
