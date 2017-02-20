package com.boostcamp.mytwitter.mytwitter.search.model;
/**
 * Created by DaHoon on 2017-02-18.
 */

public class TwitterSearchDTO {

    private RankedTwitList[] rankedTwitList;

    public RankedTwitList[] getRankedTwitList ()
    {
        return rankedTwitList;
    }

    public void setRankedTwitList (RankedTwitList[] rankedTwitList)
    {
        this.rankedTwitList = rankedTwitList;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [rankedTwitList = "+rankedTwitList+"]";
    }

    public class RankedTwitList
    {
        private String id;

        private String rtTwitCount;

        private String body;

        private String rtRank;

        private String owner;

        private String twitId;

        private String ownerProfileImgUrl;

        private String rtCount;

        private String registDate;

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getRtTwitCount ()
        {
            return rtTwitCount;
        }

        public void setRtTwitCount (String rtTwitCount)
        {
            this.rtTwitCount = rtTwitCount;
        }

        public String getBody ()
        {
            return body;
        }

        public void setBody (String body)
        {
            this.body = body;
        }

        public String getRtRank ()
        {
            return rtRank;
        }

        public void setRtRank (String rtRank)
        {
            this.rtRank = rtRank;
        }

        public String getOwner ()
        {
            return owner;
        }

        public void setOwner (String owner)
        {
            this.owner = owner;
        }

        public String getTwitId ()
        {
            return twitId;
        }

        public void setTwitId (String twitId)
        {
            this.twitId = twitId;
        }

        public String getOwnerProfileImgUrl ()
        {
            return ownerProfileImgUrl;
        }

        public void setOwnerProfileImgUrl (String ownerProfileImgUrl)
        {
            this.ownerProfileImgUrl = ownerProfileImgUrl;
        }

        public String getRtCount ()
        {
            return rtCount;
        }

        public void setRtCount (String rtCount)
        {
            this.rtCount = rtCount;
        }

        public String getRegistDate ()
        {
            return registDate;
        }

        public void setRegistDate (String registDate)
        {
            this.registDate = registDate;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+", rtTwitCount = "+rtTwitCount+", body = "+body+", rtRank = "+rtRank+", owner = "+owner+", twitId = "+twitId+", ownerProfileImgUrl = "+ownerProfileImgUrl+", rtCount = "+rtCount+", registDate = "+registDate+"]";
        }
    }

}
