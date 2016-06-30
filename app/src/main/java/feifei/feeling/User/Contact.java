/*
 * Contact.java
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

package feifei.feeling.user;

/**
 * Simple POJO holding the information of a contact or friend
 * <p/>
 * Created by joseluisugia on 26/09/15.
 */
public class Contact {

    private final int avatarResource;
    private final String name;
    private final String phoneNumber;
    private final boolean hasRecentConversation;

    public Contact(String name, String phoneNumber, boolean hasRecentConversation) {
        this(name, phoneNumber, hasRecentConversation, 0);
    }

    public Contact(String name, String phoneNumber, boolean hasRecentConversation, int avatarResource) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.hasRecentConversation = hasRecentConversation;
        this.avatarResource = avatarResource;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean hasRecentConversation() {
        return hasRecentConversation;
    }
}
