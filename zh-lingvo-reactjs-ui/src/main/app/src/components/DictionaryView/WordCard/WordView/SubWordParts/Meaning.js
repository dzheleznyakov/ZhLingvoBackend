import React from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './Meaning.module.scss';

import { EditableRemark } from '.';
import { EditableTranslation, NULL_TRANSLATION } from '.';
import { EditableExample, NULL_EXAMPLE } from '.';
import { meaningType } from '../wordTypes';
import * as selectors from '../../../../../store/selectors';

const TRANSLATIONS_PATH_SEGMENT = 'translations';
const EXAMPLES_PATH_SEGMENT = 'examples';

const getTranslations = (meaning, path, isEditing) => {
    const translations = (meaning.translations || [])
        .sort((a, b) => {
            if (a === null)
                return 1;
            if (b === null)
                return -1;
            return a.id < b.id ? -1 : 1;
        })
        .map((tr, i) => (tr === null ? null :
            <EditableTranslation 
                key={tr.id} 
                path={[...path, TRANSLATIONS_PATH_SEGMENT, `${i}`]} 
                entry={tr}
                postfix={i < meaning.translations.length - 1 ? '; ': ' '}
            />
        ));
    if (isEditing)
        translations.push(<EditableTranslation
            key="translation-new"
            path={[...path, TRANSLATIONS_PATH_SEGMENT, `${meaning.translations.length}`]}
            entry={NULL_TRANSLATION}
        />);
    return translations;
};

const getExamples = (meaning, path, isEditing) => {
    const examples = (meaning.examples || [])
        .sort((a, b) => {
            if (a === null)
                return 1;
            if (b === null)
                return -1;
            return a.id < b.id ? -1 : 1;
        })
        .map((ex, i) => (ex === null ? null :
            <EditableExample 
                key={ex.id} 
                path={[...path, EXAMPLES_PATH_SEGMENT, `${i}`]} 
                entry={ex}
            />
        ));
    if (isEditing)
        examples.push(<EditableExample
            key="example-new"
            path={[...path, EXAMPLES_PATH_SEGMENT, `${meaning.translations.length}`]}
            entry={NULL_EXAMPLE}
        />);
    return examples;
};

const Meaning = props => {
    const { meaning, path } = props;
    const isEditing = useSelector(selectors.isEditingSelector);

    const remarkPath = [...path, 'remark'];
    const remark = <EditableRemark value={meaning.remark} path={remarkPath} />;
    const translations = getTranslations(meaning, path, isEditing);
    const examples = getExamples(meaning, path, isEditing);

    return (
        <li className={classes.Meaning}>
            {remark}
            {translations}
            {examples}
        </li>
    );
};

Meaning.propTypes = {
    meaning: meaningType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default Meaning;
