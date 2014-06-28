/*
 * Copyright 2014 Christophe Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.cpollet.jixture.sample.service.impl;

import net.cpollet.jixture.sample.da.dao.UserDao;
import net.cpollet.jixture.sample.service.UserService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Christophe Pollet
 */
public class SimpleUserService implements UserService {
	private UserDao userDao;

	@Override
	public int getUsersCount() {
		return userDao.findAll().size();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
	public int getCommittedUsersCount() {
		return userDao.findAll().size();
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
