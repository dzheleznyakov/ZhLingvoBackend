import React from 'react';
import { useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';

import classes from './RecordList.module.scss';

import { useActionOnMount } from '../../../hooks';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';
import ListView from '../../Common/ListView/ListView';
import RecordListItem from './RecordListItem/RecordListItem';

const RecordList = () => {
    const { qid: quizId, rid } = useParams();
    const dispatch = useDispatch();
    useActionOnMount(actions.fetchQuizRecords(quizId));
    const overviews = useSelector(selectors.quizRecordsOverviewsSelector);

    const items = overviews.map(overview => 
        ({ key: `${overview.id}`, node: <RecordListItem quizRecord={overview} /> }));
    const onRecordClick = index => () => {
        dispatch(actions.selectQuizRecord(index));
        const recordId = overviews[index].id;
        dispatch(actions.navigateTo(`/tutor/quiz/${quizId}/${recordId}`));
    };

    const wrapperClasses = [classes.QuizRecordListWrapper];
    if (rid !== null && rid !== undefined)
        wrapperClasses.push(classes.Active);
    
    return (
        <div className={wrapperClasses.join(' ')}>
            <ListView items={items} onItemClick={onRecordClick} />
        </div>
    );
};

export default RecordList;
